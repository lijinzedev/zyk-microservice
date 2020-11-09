package cn.zhengyk.canal.listener;

import cn.zhengyk.canal.config.EventHandlerConfig;
import cn.zhengyk.canal.event.DeleteCanalEvent;
import cn.zhengyk.canal.handler.DataDbHandler;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author by yakai.zheng
 * @Description 删除事件监听器
 */
@Slf4j
@Component
public class DeleteCanalListener extends AbstractCanalListener<DeleteCanalEvent>{

    @Autowired
    private EventHandlerConfig eventHandlerConfig;

    @Override
    protected void doSync(String schemaName, String table, RowData rowData) {
        log.info("delete :{},{}", schemaName, table);
        List<Column> afterColumnsList = rowData.getAfterColumnsList();
        Map<String, String> afterColumnsMap = columnsToMap(afterColumnsList);
        DataDbHandler dataHandler = eventHandlerConfig.getDbDataHandler(schemaName + SCHEMA_TABLE_SEPARATOR + table);
        if (dataHandler != null) {
            dataHandler.delete(afterColumnsMap);
        }
    }

}
