export interface GroupWithMembership {
  id: string;
  name: string;
  pfpUrl?: string;
  ownerId: string;
  createdAt: string;
  inviteCode: string;
  description?: string;
  userRole: 'ADMIN' | 'MEMBER';
  joinedAt: string;
}
