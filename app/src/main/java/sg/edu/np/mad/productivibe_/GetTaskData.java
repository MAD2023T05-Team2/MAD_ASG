package sg.edu.np.mad.productivibe_;

import java.util.List;

public interface GetTaskData {

    void onDataLoaded(List<Task> taskList);

    void onError(String errorMsg);
}


