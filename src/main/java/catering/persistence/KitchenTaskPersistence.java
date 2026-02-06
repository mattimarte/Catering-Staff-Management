package catering.persistence;

import catering.businesslogic.kitchen.Assignment;
import catering.businesslogic.kitchen.KitchenTaskEventReceiver;
import catering.businesslogic.kitchen.KitchenSheet;
import catering.businesslogic.kitchen.KitchenTask;

public class KitchenTaskPersistence implements KitchenTaskEventReceiver {

    @Override
    public void updateSheetGenerated(KitchenSheet summarySheet) {
        KitchenSheet.saveNewSumSheet(summarySheet);
    }

    @Override
    public void updateTaskAdded(KitchenSheet currentSumSheet, KitchenTask added) {
        KitchenTask.saveNewTask(currentSumSheet.getId(), added, currentSumSheet.getTaskPosition(added));
    }

    @Override
    public void updateTaskListSorted(KitchenSheet currentSumSheet) {
        KitchenSheet.updateTaskList(currentSumSheet);
    }

    @Override
    public void updateAssignmentAdded(KitchenSheet currentSumSheet, Assignment a) {
        Assignment.saveNewAssignment(currentSumSheet.getId(), a);
    }

    @Override
    public void updateAssignmentChanged(Assignment a) {
        Assignment.updateAssignment(a);
    }

    @Override
    public void updateAssignmentDeleted(Assignment ass) {
        Assignment.deleteAssignment(ass);
    }

    @Override
    public void updateTaskChanged(KitchenTask task) {
        KitchenTask.updateTaskChanged(task);
    }

}