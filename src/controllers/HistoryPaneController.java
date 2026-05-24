/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import app.AppContext;
import datastructures.CustomStack;
import managers.Action;
import managers.HistoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryPaneController {

    @FXML
    private ListView<String> historyListView;

    @FXML
    private Spinner<Integer> undoSpinner;

    @FXML
    private Button undoButton;

    // 持有全局单例上下文
    private AppContext appContext;

    @FXML
    public void initialize() {
        // 1. 配置指南要求的 Spinner 范围 1-20，默认值为 1
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        undoSpinner.setValueFactory(valueFactory);
        undoSpinner.setEditable(true); // 允许用户直接输入数字
        
        // 2. 配置指南要求的历史记录为空时的文字提示
        historyListView.setPlaceholder(new Label("No actions yet - try Process Entry"));
    }

    /**
     * Called once by MainController after FXML load completes,
     * passing in the shared AppContext. Triggers the first refresh.
     */
    public void setContext(AppContext appContext) {
        this.appContext = appContext;
        refresh();
    }

    @FXML
    public void refresh() {
        if (appContext == null || appContext.getHistoryManager() == null) return;

        HistoryManager historyManager = appContext.getHistoryManager();
        ObservableList<String> items = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        // ======= 🌟 核心算法：利用临时栈进行倒序读取，同时不破坏原栈顺序 🌟 =======
        CustomStack<Action> tempStack = new CustomStack<>();

        // 步骤 A：把 historyManager 里的记录逐个 pop 出来，放入临时栈
        // 因为原本最新的在最上面，所以最先 pop 出来的就是最新记录
        while (historyManager.size() > 0) {
            Action action = historyManager.popLast();
            if (action == null) break;
            
            tempStack.push(action);

            // 按照指南要求的格式进行字符串组装：[HH:mm:ss] ActionDescription
            String timeStr = sdf.format(new Date(action.getTimestamp()));
            String rowText = "[" + timeStr + "] " + action.getDescription();
            items.add(rowText); // 塞入前端的显示列表中
        }

        // 步骤 B：把临时栈里的数据重新放回 historyManager，完美复原底层数据结构
        while (!tempStack.isEmpty()) {
            historyManager.recordAction(tempStack.pop());
        }
        // ====================================================================

        // 将处理好的最新最前（newest-first）的列表数据扔给 UI 渲染
        historyListView.setItems(items);
    }

    @FXML
    private void handleUndo() {
        if (appContext == null || appContext.getGateManager() == null) return;
        
        int steps = undoSpinner.getValue();
        
        // 1. 调用底层的撤销逻辑
        appContext.getGateManager().undoLast(steps);
        System.out.println("Undoing last " + steps + " steps.");
        
        // 2. 撤销成功后，触发全局刷新（这里需要配合 Member 5 写的 MainController）
        // 如果你们的 AppContext 里有全局刷新的引用，可以解除下行的注释：
        // appContext.getMainController().refreshAll();
        
        // 兜底：先刷新你自己的历史面板
        refresh();
    }
}
