import { useState, useEffect } from "react";
import { NavBar } from "@/features/common/components/nav-bar";
import { GroupScrollable } from "../components/group-scrollable";
import { CreateGroupDialog } from "../components/create-group-dialog";
import { JoinGroupDialog } from "../components/join-group-dialog";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { 
  getUserGroups, 
  createGroup, 
  leaveGroup,
  joinGroup
} from "../repository/profile-repo";
import type { 
  CreateGroupRequest,
  JoinGroupRequest
} from "../entity";
import type { GroupWithMembership } from "../entity/group-with-membership";
import { 
  showCreateGroupSuccessToast
} from "../components/create-group-toast";
import { 
  showLeaveGroupSuccessToast, 
  showLeaveGroupErrorToast 
} from "../components/leave-group-toast";
import { 
  showJoinGroupSuccessToast
} from "../components/join-group-toast";

export default function DashboardPage() {
  const [groups, setGroups] = useState<GroupWithMembership[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isCreatingGroup, setIsCreatingGroup] = useState(false);
  const [isJoiningGroup, setIsJoiningGroup] = useState(false);
  const [leavingGroupId, setLeavingGroupId] = useState<string | null>(null);
  const [createGroupError, setCreateGroupError] = useState<string>("");
  const [joinGroupError, setJoinGroupError] = useState<string>("");

  useEffect(() => {
    loadGroups();
  }, []);

  const loadGroups = async () => {
    try {
      setIsLoading(true);
      const userGroups = await getUserGroups();
      setGroups(userGroups);
    } catch (error) {
      console.error("Failed to load groups:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreateGroup = async (request: CreateGroupRequest) => {
    try {
      setIsCreatingGroup(true);
      setCreateGroupError(""); 
      const newGroup = await createGroup(request);
      await loadGroups();
      showCreateGroupSuccessToast(newGroup.name);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : "Failed to create group";
      setCreateGroupError(errorMessage);
    } finally {
      setIsCreatingGroup(false);
    }
  };

  const handleLeaveGroup = async (groupId: string) => {
    try {
      setLeavingGroupId(groupId);
      const groupToLeave = groups.find(g => g.id === groupId);
      await leaveGroup(groupId);
      setGroups(prev => prev.filter(g => g.id !== groupId));
      if (groupToLeave) {
        showLeaveGroupSuccessToast(groupToLeave.name);
      }
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : "Failed to leave group";
      showLeaveGroupErrorToast(errorMessage);
    } finally {
      setLeavingGroupId(null);
    }
  };

  const handleJoinGroup = async (request: JoinGroupRequest) => {
    try {
      setIsJoiningGroup(true);
      setJoinGroupError("");
      const joinedGroup = await joinGroup(request);
      // reload to update w new group
      await loadGroups();
      showJoinGroupSuccessToast(joinedGroup.name);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : "Failed to join group";
      setJoinGroupError(errorMessage);
    } finally {
      setIsJoiningGroup(false);
    }
  };

  return (
    <div className="min-h-svh">
      <NavBar />
      <div className="p-6 max-w-6xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2">Welcome back!</h1>
          <p className="text-muted-foreground">
            It's time for another crawl...
          </p>
        </div>

        {/* groups section */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle >Your Groups</CardTitle>
              <div className="flex gap-2">
                <JoinGroupDialog
                  onJoinGroup={handleJoinGroup}
                  isLoading={isJoiningGroup}
                  error={joinGroupError}
                  onSuccess={() => setJoinGroupError("")}
                />
                <CreateGroupDialog
                  onCreateGroup={handleCreateGroup}
                  isLoading={isCreatingGroup}
                  error={createGroupError}
                  onSuccess={() => setCreateGroupError("")}
                />
              </div>
            </div>
          </CardHeader>
          <CardContent>
            {isLoading ? (
              <div className="flex items-center justify-center h-32">
                <p className="text-muted-foreground">Loading groups...</p>
              </div>
            ) : (
              <GroupScrollable
                groups={groups}
                onLeaveGroup={handleLeaveGroup}
                leavingGroupId={leavingGroupId || undefined}
              />
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
