export interface CreateGroupRequest {
  name: string;
  pfpUrl?: string;
  description?: string;
}

export interface JoinGroupRequest {
  inviteCode: string;
  highlightColor?: string;
}
