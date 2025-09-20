import { apiCall } from "@/lib/api-helper";
import type { Group, CreateGroupRequest, JoinGroupRequest } from "../entity";
import type { GroupWithMembership } from "../entity/group-with-membership";

export const getAllGroups = async (): Promise<Group[]> => {
  const response = await apiCall('/groups');
  return response.json();
};

export const getUserGroups = async (): Promise<GroupWithMembership[]> => {
  const response = await apiCall('/groups/my-groups');
  return response.json();
};

export const createGroup = async (request: CreateGroupRequest): Promise<Group> => {
  const response = await apiCall('/groups', {
    method: 'POST',
    body: JSON.stringify(request),
  });
  return response.json();
};

export const getGroupByInviteCode = async (inviteCode: string): Promise<Group> => {
  const response = await apiCall(`/groups/by-invite/${inviteCode}`);
  return response.json();
};

export const joinGroup = async (request: JoinGroupRequest): Promise<Group> => {
  // find group by invite code
  const group = await getGroupByInviteCode(request.inviteCode);
  
  // join group using group ID
  await apiCall(`/groups/${group.id}/members`, {
    method: 'POST',
    body: JSON.stringify(request),
  });
  
  return group; // Return the group info for use in toasts
};

export const leaveGroup = async (groupId: string): Promise<void> => {
  await apiCall(`/groups/${groupId}/members/me`, {
    method: 'DELETE',
  });
};

