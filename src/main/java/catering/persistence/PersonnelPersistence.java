package catering.persistence;

import catering.businesslogic.personnel.*;

public class PersonnelPersistence implements PersonnelEventReceiver {
    @Override
    public void onEmployeeCreated(Employee e) {
        e.save();
    }

    @Override
    public void onEmployeeUpdated(Employee e) {
        e.update();
    }

    @Override
    public void onEmployeeRemoved(Employee e) {
        e.delete();
    }

    @Override
    public void onContractCreated(Contract c) {
        c.save();
    }

    @Override
    public void onVacationRequestUpdated(VacationRequest vr) {
        vr.update();
    }

    @Override
    public void onShiftBoardCreated(ShiftBoard sb) {
        sb.save();
    }

    @Override
    public void onRoleAssignmentCreated(RoleAssignment ra) {
        ra.update();
    }

    @Override
    public void onRoleAssignmentRemoved(RoleAssignment ra) {
        ra.delete();
    }
}
