function  [ RETURN_CODE,PROCESS_ID ] = CREATE_PROCESS(ATTRIBUTES)

        global RETURN_CODE_TYPE;
        global OPERATING_MODE_TYPE;
        global PROCESS_STATE_TYPE;
        global Set;
        global Current_Partition_Status;

        global SYSTEM_NUMBER_OF_PROCESSES;
        global MAX_NUMBER_OF_PROCESSES;

        if SYSTEM_NUMBER_OF_PROCESSES == MAX_NUMBER_OF_PROCESSES
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_CONFIG;
            return;
        end

        if ismemberofset(Set.Process_Name_Set,ATTRIBUTES.NAME)==1
            RETURN_CODE = RETURN_CODE_TYPE.NO_ACTION;
            return;
        end

        if invalid_stack_size(ATTRIBUTES.STACK_SIZE)==1
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_PARAM;
            return;
        end

        if invalid_base_priority(ATTRIBUTES.BASE_PRIORITY)==1
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_PARAM;
            return;
        end

        if invalid_period(ATTRIBUTES.PERIOD)==1
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_CONFIG;
            return;
        end

        if invalid_time_capacity(ATTRIBUTES.TIME_CAPACITY)==1
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_PARAM;
            return;
        end

        if Current_Partition_Status.OPERATING_MODE == OPERATING_MODE_TYPE.NORMAL
            RETURN_CODE = RETURN_CODE_TYPE.INVALID_PARAM;
            return;
        end

        id = generate_id();

        PRO_ATT = PROCESS_ATTRIBUTE_TYPE(id,ATTRIBUTES.PERIOD,ATTRIBUTES.TIME_CAPACITY,ATTRIBUTES.ENTRY_POINT,ATTRIBUTES.STACK_SIZE,ATTRIBUTES.BASE_PRIORITY,ATTRIBUTES.DEADLINE,ATTRIBUTES.NAME);
        PRO_STA = PROCESS_STATUS_TYPE(id,0,8,PROCESS_STATE_TYPE.DORMANT,ATTRIBUTES.NAME);

        insert_into(Set.Process_Attribute_Set,PRO_ATT);
        insert_into(Set.Process_Status_Set,PRO_STA);
        insert_into(Set.Process_Name_Set,PRO_ATT.NAME);
        insert_into(Set.Process_Id_Set,PRO_ATT.ID);
        insert_into(Set.Dormant_Processes_Set,PRO_ATT.ID);

        SYSTEM_NUMBER_OF_PROCESSES = SYSTEM_NUMBER_OF_PROCESSES + 1;

        initialize_Process_Context(PRO_ATT.ID);
        initialize_Process_Stack(PRO_ATT.ID);

        PROCESS_ID = PRO_ATT.ID;
        RETURN_CODE = RETURN_CODE_TYPE.NO_ERROR;

end
