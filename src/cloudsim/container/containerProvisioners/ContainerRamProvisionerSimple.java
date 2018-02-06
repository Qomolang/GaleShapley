package cloudsim.container.containerProvisioners;

import cloudsim.container.core.Container;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sareh
 */
public class ContainerRamProvisionerSimple extends ContainerRamProvisioner {
    /**
     * The RAM table.
     */
    private Map<String, Float> containerRamTable;

    /**
     * @param availableRam the available ram
     */
    public ContainerRamProvisionerSimple(float availableRam) {
        super(availableRam);
        setContainerRamTable(new HashMap<String, Float>());
    }

    /**
     * @param container the container
     * @param ram       the ram
     * @return
     * @see ContainerRamProvisioner#allocateRamForContainer(Container, float)
     */
    @Override
    public boolean allocateRamForContainer(Container container,
                                           float ram) {
        float maxRam = container.getRam();

        if (ram >= maxRam) {
            ram = maxRam;
        }

        deallocateRamForContainer(container);

        if (getAvailableVmRam() >= ram) {
            setAvailableVmRam(getAvailableVmRam() - ram);
            getContainerRamTable().put(container.getUid(), ram);
            container.setCurrentAllocatedRam(getAllocatedRamForContainer(container));
            return true;
        }

        container.setCurrentAllocatedRam(getAllocatedRamForContainer(container));

        return false;
    }

    /**
     * @param container the container
     * @return
     * @link ContainerRamProvisioner#getAllocatedRamForContainer(Container)
     */
    @Override
    public float getAllocatedRamForContainer(Container container) {
        if (getContainerRamTable().containsKey(container.getUid())) {
            return getContainerRamTable().get(container.getUid());
        }
        return 0;
    }

    /**
     * @param container the container
     * @see ContainerRamProvisioner#deallocateRamForContainer(Container)
     */
    @Override
    public void deallocateRamForContainer(Container container) {
        if (getContainerRamTable().containsKey(container.getUid())) {
            float amountFreed = getContainerRamTable().remove(container.getUid());
            setAvailableVmRam(getAvailableVmRam() + amountFreed);
            container.setCurrentAllocatedRam(0);
        }
    }

    /**
     * @see ContainerRamProvisioner#deallocateRamForAllContainers()
     */
    @Override
    public void deallocateRamForAllContainers() {
        super.deallocateRamForAllContainers();
        getContainerRamTable().clear();
    }


    /**
     * @param container the container
     * @param ram       the vm's ram
     * @return
     * @see ContainerRamProvisioner#isSuitableForContainer(Container, float)
     */
    @Override
    public boolean isSuitableForContainer(Container container,
                                          float ram) {
        float allocatedRam = getAllocatedRamForContainer(container);
        boolean result = allocateRamForContainer(container, ram);
        deallocateRamForContainer(container);
        if (allocatedRam > 0) {
            allocateRamForContainer(container, allocatedRam);
        }
        return result;
    }


    /**
     * @return the containerRamTable
     */
    protected Map<String, Float> getContainerRamTable() {
        return containerRamTable;
    }

    /**
     * @param containerRamTable the containerRamTable to set
     */
    protected void setContainerRamTable(Map<String, Float> containerRamTable) {
        this.containerRamTable = containerRamTable;
    }

}
