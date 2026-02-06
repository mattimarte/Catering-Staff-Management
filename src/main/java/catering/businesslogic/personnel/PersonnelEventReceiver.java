package catering.businesslogic.personnel;

import catering.businesslogic.personnel.*;

public interface PersonnelEventReceiver {

    void onEmployeeCreated(Employee e);

    void onEmployeeUpdated(Employee e);

    void onEmployeeRemoved(Employee e);

    void onContractCreated(Contract c);

    void onVacationRequestUpdated(VacationRequest vr);

    void onShiftBoardCreated(ShiftBoard sb);

    void onRoleAssignmentCreated(RoleAssignment ra);

    void onRoleAssignmentRemoved(RoleAssignment ra);
}
