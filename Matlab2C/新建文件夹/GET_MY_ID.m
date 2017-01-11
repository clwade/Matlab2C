function [ RETURN_CODE,PROCESS_ID ] = GET_MY_ID()

        global RETURN_CODE_TYPE;

        global Running_Processes_Set;
        global NULL_PROCESS_ID;
        global ERROR_HANDLER_PROCESS_ID;
        global BASEROW;

        index = 1;
        if  numel(Running_Processes_Set)==0 || Running_Processes_Set{BASEROW,index}.ID == ERROR_HANDLER_PROCESS_ID
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_MODE;
            PROCESS_ID = NULL_PROCESS_ID;
            return;
        end

        RETURN_CODE = RETURN_CODE_TYPE.NO_ERROR;
        PROCESS_ID = Running_Processes_Set{BASEROW,index}.ID;

        return;

end

