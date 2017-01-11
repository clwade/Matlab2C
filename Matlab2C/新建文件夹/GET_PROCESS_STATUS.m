function [ RETURN_CODE,PROCESS_STATUS ] = GET_PROCESS_STATUS(PROCESS_ID)

        global RETURN_CODE_TYPE;
        global Process_Status_Set;
        global Process_Attribute_Set;
        global Set;

        global SYSTEM_NUMBER_OF_PROCESSES;
        global BASEROW;

        if ismemberofset(Set.Process_Id_Set,PROCESS_ID) == 0
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_PARAM;
            PROCESS_STATUS = NaN;
            return;
        end

        index = 1;
        while index <= SYSTEM_NUMBER_OF_PROCESSES
           if Process_Status_Set{BASEROW,index}.ID == PROCESS_ID
                process_status.DEADLINE_TIME = Process_Status_Set{BASEROW,index}.DEADLINE_TIME;
                process_status.CURRENT_PRIORITY = Process_Status_Set{BASEROW,index}.CURRENT_PRIORITY;
                process_status.PROCESS_STATE = Process_Status_Set{BASEROW,index}.PROCESS_STATE;
                break;
            else
                index = index + 1;
            end 
        end

        index = 1;
        while index <= SYSTEM_NUMBER_OF_PROCESSES
            if Process_Attribute_Set{BASEROW,index}.ID == PROCESS_ID
                process_status.ATTRIBUTES = Process_Attribute_Set{BASEROW,index};
                break;
            else
                index = index + 1;
            end
        end

        RETURN_CODE = RETURN_CODE_TYPE.NO_ERROR;
        PROCESS_STATUS = process_status;

        return;

end	
