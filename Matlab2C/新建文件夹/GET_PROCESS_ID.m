function [ RETURN_CODE,PROCESS_ID ] = GET_PROCESS_ID (PROCESS_NAME)

        global RETURN_CODE_TYPE;

        global Set;
        global Process_Attribute_Set;
        global SYSTEM_NUMBER_OF_PROCESSES;
        global BASEROW;

        if ismemberofset(Set.Process_Name_Set,PROCESS_NAME)==0
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_CONFIG;
            PROCESS_ID = -1;
            return;
        end

        index = 1;
        id = -1;
        while index <= SYSTEM_NUMBER_OF_PROCESSES
            if Process_Attribute_Set{BASEROW,index}.NAME == PROCESS_NAME
                id = Process_Attribute_Set{BASEROW,index}.ID;
                break;
            else
                index = index + 1;
            end  
        end

        RETURN_CODE = RETURN_CODE_TYPE.NO_ERROR;
        PROCESS_ID = id;
        return;

end
