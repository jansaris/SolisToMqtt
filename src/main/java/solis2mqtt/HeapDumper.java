package solis2mqtt;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

@Slf4j
public class HeapDumper {

    /**
    * Call this method from your application whenever you
    * want to dump the memory usage to the logging
    */
    static void dumpHeap() {
        log.debug("Heap: {}", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        log.debug("NonHeap: {}", ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
        List<MemoryPoolMXBean> beans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean bean: beans) {
            log.debug("{}: {}", bean.getName(), bean.getUsage());
        }

        for (GarbageCollectorMXBean bean: ManagementFactory.getGarbageCollectorMXBeans()) {
            log.debug("{}: {}-{}", bean.getName(), bean.getCollectionCount(), bean.getCollectionTime());
        }
    }
}