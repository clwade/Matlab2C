function [ RETURN_CODE ]= SET_PRIORITY(PROCESS_ID,PRIORITY)

        global RETURN_CODE_TYPE;
        global PROCESS_STATE_TYPE;

        global Set;
        global Process_Status_Set;
        global Current_Partition_Status;         

        global BASEROW;
        global SYSTEM_NUMBER_OF_PROCESSES;
        global PROCESS_SCHEDULING_FLAG;

        if ismemberofset(Set.Process_Id_Set,PROCESS_ID) == 0
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_PARAM;
            return;
        end

        if  invalid_priority(PRIORITY) == 1
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_PARAM;	
            return;
        end

        index = 1;
        while index <= SYSTEM_NUMBER_OF_PROCESSES
           if Process_Status_Set{BASEROW,index}.ID == PROCESS_ID
                PROCESS_STATE = Process_Status_Set{BASEROW,index}.PROCESS_STATE;
                break;
            else
                index = index + 1;
            end 
        end  

        if PROCESS_STATE == PROCESS_STATE_TYPE.DORMANT
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_MODE;
            return;
        end

        Process_Status_Set{BASEROW,index}.CURRENT_PRIORITY = PRIORITY;

        if Current_Partition_Status.LOCK_LEVEL == 0
            PROCESS_SCHEDULING_FLAG = 1;
        end

        RETURN_CODE = RETURN_CODE_TYPE.NO_ERROR;
        return;
         
end
	 
			
		 	
