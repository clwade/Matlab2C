function [ ] = insert_into (Set_Collection,Element)

global Set;
global Process_Attribute_Set;
global Dormant_Processes_Set;
global Ready_Processes_Set;
global Waiting_Processes_Set;
global Running_Processes_Set;
global Process_Name_Set;
global Process_Id_Set;
global Process_Status_Set;
global BASEROW;

if Set_Collection == Set.Dormant_Processes_Set
    Dormant_Processes_Set{BASEROW,numel(Dormant_Processes_Set)+1}.ID = Element;
elseif Set_Collection == Set.Ready_Processes_Set
    Ready_Processes_Set{BASEROW,numel(Ready_Processes_Set)+1}.ID = Element;
elseif Set_Collection == Set.Waiting_Processes_Set
    Waiting_Processes_Set{BASEROW,numel(Waiting_Processes_Set)+1}.ID = Element;
elseif Set_Collection == Set.Running_Processes_Set
    Running_Processes_Set{BASEROW,numel(Running_Processes_Set)+1}.ID = Element;
elseif Set_Collection == Set.Process_Name_Set
    Process_Name_Set{BASEROW,numel(Process_Name_Set)+1}.NAME = Element;
elseif Set_Collection == Set.Process_Id_Set
    Process_Id_Set{BASEROW,numel(Process_Id_Set)+1}.ID = Element;
elseif Set_Collection == Set.Process_Status_Set
    Process_Status_Set{BASEROW,numel(Process_Status_Set)+1} = Element;
else
    Process_Attribute_Set{BASEROW,numel(Process_Attribute_Set)+1} = Element;
end

end

