package catering.businesslogic;

import catering.businesslogic.event.Event;
import catering.businesslogic.event.Service;
import catering.businesslogic.kitchen.KitchenSheet;
import catering.businesslogic.personnel.*;
import catering.businesslogic.shift.Shift;
import catering.businesslogic.user.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonnelManager {

    private List<PersonnelEventReceiver> eventReceivers;
    private ShiftBoard currentShiftBoard;

    public PersonnelManager(){
        this.eventReceivers = new ArrayList<>();
    }

    public void addEventReceiver(PersonnelEventReceiver per) {
        if(per != null && !eventReceivers.contains(per)) {
            eventReceivers.add(per);
        }
    }

    public void removeEventReceiver(PersonnelEventReceiver per) {
        eventReceivers.remove(per);
    }

    public Employee updatePermanentList(String name, String contact) throws UseCaseLogicException{
        User currentUser = CatERing.getInstance().getUserManager().getCurrentUser();
        if(currentUser == null || !currentUser.isOwner()) {
            throw new UseCaseLogicException("Access Denied: User is not owner!");
        }
        Employee newEmployee = new Employee(name, contact);
        notifyNewEmployee(newEmployee);
        return newEmployee;
    }

    public Contract signNewContract(Employee employee, String address, String fiscalCode, boolean permanent, Date startDate) throws UseCaseLogicException {
        if(employee == null) {
            throw new UseCaseLogicException("Employee cannot be null!");
        }
        Contract newContract = new Contract(permanent, startDate);
        employee.addContract(newContract);
        employee.updateData(employee.getName(), address, fiscalCode, employee.getContact());

        notifyNewContract(newContract);
        notifyNewEmployee(employee);

        return newContract;
    }

    public Contract signNewContract(Employee employee, String address, String fiscalCode, boolean permanent, Date startDate, Date endDate) throws UseCaseLogicException {
        if(employee == null) {
            throw new UseCaseLogicException("Employee cannot be null!");
        }
        Contract newContract = new Contract(permanent, startDate, endDate);
        employee.addContract(newContract);
        employee.updateData(employee.getName(), address, fiscalCode, employee.getContact());

        notifyNewContract(newContract);
        notifyNewEmployee(employee);

        return newContract;
    }

    public void acceptVacationRequest(VacationRequest vr) throws UseCaseLogicException {
        if(vr == null) {
            throw new UseCaseLogicException("VacationRequest cannot be null!");
        }
        Employee employee = vr.getEmployee();
        vr.approve();
        employee.updateRemainingVacDays(vr.getStartDate(), vr.getEndDate());

        notifyUpdatedVacationRequest(vr);
        notifyNewEmployee(employee);
    }

    public ShiftBoard compileShiftBoard(Service service) throws UseCaseLogicException {
        User currentUser = CatERing.getInstance().getUserManager().getCurrentUser();
        if(currentUser == null || !currentUser.isOrganizer()) {
            throw new UseCaseLogicException("Access Denied: User is not organizer!");
        }
        if(service == null) {
            throw new UseCaseLogicException("Service cannot be null!");
        }

        ShiftBoard newBoard = new ShiftBoard(service, currentUser);
        this.currentShiftBoard = newBoard;

        notifyNewShiftBoard(newBoard);
        return newBoard;
    }

    public RoleAssignment assignPermanentRole(Employee employee, Role role) throws UseCaseLogicException {
        if(this.currentShiftBoard == null) {
            throw new UseCaseLogicException("No active shift board available! Please, compile one first");
        }
        if(employee == null || role == null) {
            throw new UseCaseLogicException("Employee or role cannot be null!");
        }

        RoleAssignment newAssignment = new RoleAssignment(employee, role);
        this.currentShiftBoard.addAssignment(newAssignment);

        notifyRoleAssignment(newAssignment);
        return newAssignment;
    }

    public RoleAssignment assignPermanentRole(Employee employee, Role role, Shift shift) throws UseCaseLogicException {
        if(this.currentShiftBoard == null) {
            throw new UseCaseLogicException("No active shift board available! Please, compile one first");
        }
        if(employee == null || role == null) {
            throw new UseCaseLogicException("Employee or role cannot be null!");
        }

        RoleAssignment newAssignment = new RoleAssignment(employee, role, shift);
        this.currentShiftBoard.addAssignment(newAssignment);

        notifyRoleAssignment(newAssignment);
        return newAssignment;
    }

    public ShiftBoard getCurrentShiftBoard(){
        return currentShiftBoard;
    }

    public List<Note> viewPerformanceNotes(Employee employee){
        return null;
    }

    public Employee viewEmployeeInformations(Employee employee){
        return null;
    }

    public void rejectVacationRequest(VacationRequest vacationRequest){
        vacationRequest.reject();
    }

    public Note addPerformanceNote(Employee employee, Event event, Date date, String text){
        return null;
    }

    public List<Employee> viewPermanentEmployeeList(){
        return null;
    }

    public boolean checkEmployeeAvailability(Employee employee){
        return employee.isAssigned(); //DA MODIFICARE IL METODO isAssigned
    }

    public List<Employee> viewOccasionalEmployeeList(){
        return null;
    }

    public RoleAssignment assignOccasionalRole(Employee employee, Role role){
        return null;
    }

    public RoleAssignment assignOccasionalRole(Employee employee, Role role, Shift shift) {
        return null;
    }

    public void removeOccasionalEmployee(Employee employee){

    }

    public void updateOccasionalEmployee(Employee employee, String name){

    }

    public void updateOccasionalEmployee(Employee employee, String name, String address, String fiscalCode, String contact){

    }

    // Getters and Setters


    // Notify methods
    private void notifyNewEmployee(Employee e){
        for(PersonnelEventReceiver per : eventReceivers){
            per.onEmployeeCreated(e);
        }
    }

    private void notifyUpdatedEmployee(Employee e){
        for(PersonnelEventReceiver per : eventReceivers){
            per.onEmployeeUpdated(e);
        }
    }

    private void notifyNewContract(Contract c){
        for(PersonnelEventReceiver per : eventReceivers){
            per.onContractCreated(c);
        }
    }

    private void notifyUpdatedVacationRequest(VacationRequest vr){
        for(PersonnelEventReceiver per : eventReceivers){
            per.onVacationRequestUpdated(vr);
        }
    }

    private void notifyNewShiftBoard(ShiftBoard sb){
        for(PersonnelEventReceiver per : eventReceivers){
            per.onShiftBoardCreated(sb);
        }
    }

    private void notifyRoleAssignment(RoleAssignment ra){
        for(PersonnelEventReceiver per : eventReceivers){
            per.onRoleAssignmentCreated(ra);
        }
    }

    private void notifyNewNote(Note note){}

    private void notifyRemovedEmployee(Employee employee){}

    public void reset(){
        this.currentShiftBoard = null;
    }
}

/**
 * TODO:
 * - Le notify su PersonnelEventReceiver
 * - Completare metodi vuoti (es. viewEmployeeInformation)
 */