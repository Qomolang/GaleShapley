package cloudsim.container.lists;

import cloudsim.Log;
import cloudsim.container.containerProvisioners.ContainerPe;
import cloudsim.container.core.Container;

import java.util.List;

/**
 * Created by sareh on 10/07/15.
 */
public class ContainerPeList {


    /**
     * Gets MIPS Rating for a specified Pe ID.
     *
     * @param id     the Pe ID
     * @param peList the pe list
     * @return the MIPS rating if exists, otherwise returns -1
     * @pre id >= 0
     * @post $none
     */
    public static <T extends ContainerPe> ContainerPe getById(List<T> peList, int id) {
        for (ContainerPe pe : peList) {
            if (pe.getId() == id) {
                return pe;
            }
        }
        return null;
    }

    /**
     * Gets MIPS Rating for a specified Pe ID.
     *
     * @param id     the Pe ID
     * @param peList the pe list
     * @return the MIPS rating if exists, otherwise returns -1
     * @pre id >= 0
     * @post $none
     */
    public static <T extends ContainerPe> int getMips(List<T> peList, int id) {
        ContainerPe pe = getById(peList, id);
        if (pe != null) {
            return pe.getMips();
        }
        return -1;
    }

    /**
     * Gets total MIPS Rating for all PEs.
     *
     * @param peList the pe list
     * @return the total MIPS Rating
     * @pre $none
     * @post $none
     */
    public static <T extends ContainerPe> int getTotalMips(List<T> peList) {
        int totalMips = 0;
        for (ContainerPe pe : peList) {
            totalMips += pe.getMips();
        }
        return totalMips;
    }

    /**
     * Gets the max utilization among by all PEs.
     *
     * @param peList the pe list
     * @return the utilization
     */
    public static <T extends ContainerPe> double getMaxUtilization(List<T> peList) {
        double maxUtilization = 0;
        for (ContainerPe pe : peList) {
            double utilization = pe.getContainerPeProvisioner().getUtilization();
            if (utilization > maxUtilization) {
                maxUtilization = utilization;
            }
        }
        return maxUtilization;
    }

    /**
     * Gets the max utilization among by all PEs allocated to the VM.
     *
     * @param container the container
     * @param peList    the pe list
     * @return the utilization
     */
    public static <T extends ContainerPe> double getMaxUtilizationAmongVmsPes(List<T> peList, Container container) {
        double maxUtilization = 0;
        for (ContainerPe pe : peList) {
            if (pe.getContainerPeProvisioner().getAllocatedMipsForContainer(container) == null) {
                continue;
            }
            double utilization = pe.getContainerPeProvisioner().getUtilization();
            if (utilization > maxUtilization) {
                maxUtilization = utilization;
            }
        }
        return maxUtilization;
    }

    /**
     * Gets a Pe ID which is FREE.
     *
     * @param peList the pe list
     * @return a Pe ID if it is FREE, otherwise returns -1
     * @pre $none
     * @post $none
     */
    public static <T extends ContainerPe> ContainerPe getFreePe(List<T> peList) {
        for (ContainerPe pe : peList) {
            if (pe.getStatus() == ContainerPe.FREE) {
                return pe;
            }
        }
        return null;
    }

    /**
     * Gets the number of <tt>FREE</tt> or non-busy Pe.
     *
     * @param peList the pe list
     * @return number of Pe
     * @pre $none
     * @post $result >= 0
     */
    public static <T extends ContainerPe> int getNumberOfFreePes(List<T> peList) {
        int cnt = 0;
        for (ContainerPe pe : peList) {
            if (pe.getStatus() == ContainerPe.FREE) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Sets the Pe status.
     *
     * @param status Pe status, either <tt>Pe.FREE</tt> or <tt>Pe.BUSY</tt>
     * @param id     the id
     * @param peList the pe list
     * @return <tt>true</tt> if the Pe status has been changed, <tt>false</tt> otherwise (Pe id might
     * not be exist)
     * @pre peID >= 0
     * @post $none
     */
    public static <T extends ContainerPe> boolean setPeStatus(List<T> peList, int id, int status) {
        ContainerPe pe = getById(peList, id);
        if (pe != null) {
            pe.setStatus(status);
            return true;
        }
        return false;
    }

    /**
     * Gets the number of <tt>BUSY</tt> Pe.
     *
     * @param peList the pe list
     * @return number of Pe
     * @pre $none
     * @post $result >= 0
     */
    public static <T extends ContainerPe> int getNumberOfBusyPes(List<T> peList) {
        int cnt = 0;
        for (ContainerPe pe : peList) {
            if (pe.getStatus() == ContainerPe.BUSY) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Sets the status of PEs of this machine to FAILED. NOTE: <tt>resName</tt> and
     * <tt>machineID</tt> are used for debugging purposes, which is <b>ON</b> by default. Use
     *
     * @param resName the name of the resource
     * @param hostId  the id of this machine
     * @param failed  the new value for the "failed" parameter
     */
    public static <T extends ContainerPe> void setStatusFailed(
            List<T> peList,
            String resName,
            int hostId,
            boolean failed) {
        String status = null;
        if (failed) {
            status = "FAILED";
        } else {
            status = "WORKING";
        }

        Log.printConcatLine(resName, " - Machine: ", hostId, " is ", status);

        setStatusFailed(peList, failed);
    }

    /**
     * Sets the status of PEs of this machine to FAILED.
     *
     * @param failed the new value for the "failed" parameter
     * @param peList the pe list
     */
    public static <T extends ContainerPe> void setStatusFailed(List<T> peList, boolean failed) {
        // a loop to set the status of all the PEs in this machine
        for (ContainerPe pe : peList) {
            if (failed) {
                pe.setStatus(ContainerPe.FAILED);
            } else {
                pe.setStatus(ContainerPe.FREE);
            }
        }
    }

}