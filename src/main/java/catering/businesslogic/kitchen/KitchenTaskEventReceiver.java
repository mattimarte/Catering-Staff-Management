package catering.businesslogic.kitchen;

public interface KitchenTaskEventReceiver {

    void updateSheetGenerated(KitchenSheet kitchenSheet);

    void updateTaskAdded(KitchenSheet currentKitchenSheet, KitchenTask added);

    void updateTaskListSorted(KitchenSheet currentSumSheet);

    void updateAssignmentAdded(KitchenSheet currentSumSheet, Assignment a);

    void updateAssignmentChanged(Assignment a);

    void updateAssignmentDeleted(Assignment ass);

    void updateTaskChanged(KitchenTask task);

}