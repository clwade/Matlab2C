function [ flag ] = ismemberofset( Set_Collection,Element )

global Set;
global Dormant_Processes_Set;
global Ready_Processes_Set;
global Waiting_Processes_Set;
global Running_Processes_Set;
global Process_Name_Set;
global Process_Id_Set;

global SYSTEM_NUMBER_OF_PROCESSES;
global BASEROW;

flag = 0;

if Set_Collection == Set.Dormant_Processes_Set
    index = 1;
    while index <= SYSTEM_NUMBER_OF_PROCESSES
        if Dormant_Processes_Set{BASEROW,index}.ID == Element
            flag = 1;
            break;
        else
            index = index + 1;
        end
    end
    return;
elseif Set_Collection == Set.Ready_Processes_Set
    index = 1;
    while index <= SYSTEM_NUMBER_OF_PROCESSES
        if Ready_Processes_Set{BASEROW,index}.ID == Element
            flag = 1;
            break;
        else
            index = index + 1;
        end
    end
    return;
elseif Set_Collection == Set.Waiting_Processes_Set
    index = 1;
    while index <= SYSTEM_NUMBER_OF_PROCESSES
        if Waiting_Processes_Set{BASEROW,index}.ID == Element
            flag = 1;
            break;
        else
            index = index + 1;
        end
    end
    return;
elseif Set_Collection == Set.Running_Processes_Set
    index = 1;
    while index <= SYSTEM_NUMBER_OF_PROCESSES
        if Running_Processes_Set{BASEROW,index}.ID == Element
            flag = 1;
            break;
        else
            index = index + 1;
        end
    end
    return;
elseif Set_Collection == Set.Process_Name_Set
    index = 1;
    while index <= SYSTEM_NUMBER_OF_PROCESSES
        if Process_Name_Set{BASEROW,index}.NAME == Element
            flag = 1;
            break;
        else
            index = index + 1;
        end
    end
    return;
elseif Set_Collection == Set.Process_Id_Set
    index = 1;
    while index <= SYSTEM_NUMBER_OF_PROCESSES
        if Process_Id_Set{BASEROW,index}.ID == Element
            flag = 1;
            break;
        else
            index = index + 1;
        end
    end
    return;
end

end

