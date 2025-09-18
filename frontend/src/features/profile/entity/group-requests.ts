export interface CreateGroupRequest {
  name: string;
  pfpUrl?: string;
}

export interface JoinGroupRequest {
  inviteCode: string;
  highlightColor?: string;
}
