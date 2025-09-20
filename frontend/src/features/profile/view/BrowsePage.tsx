import { useState, useEffect } from "react";
import { NavBar } from "@/features/common/components/nav-bar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Search, ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";
import { getAllGroups, getUserGroups, joinGroup } from "../repository/profile-repo";
import type { Group, JoinGroupRequest } from "../entity";
import { BrowseGroupScrollable } from "../components/browse-group-scrollable";
import { showJoinGroupSuccessToast } from "../components/join-group-toast";

export default function BrowsePage() {
  const [groups, setGroups] = useState<Group[]>([]);
  const [userGroups, setUserGroups] = useState<Group[]>([]);
  const [filteredGroups, setFilteredGroups] = useState<Group[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [joiningGroupId, setJoiningGroupId] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    loadGroups();
  }, []);

  // Filter groups based on search query
  useEffect(() => {
    if (!searchQuery.trim()) {
      setFilteredGroups(groups);
    } else {
      const filtered = groups.filter(group =>
        group.name.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setFilteredGroups(filtered);
    }
  }, [groups, searchQuery]);

  const loadGroups = async () => {
    try {
      setIsLoading(true);
      const [allGroups, userGroupsData] = await Promise.all([
        getAllGroups(),
        getUserGroups()
      ]);
      setGroups(allGroups);
      setUserGroups(userGroupsData);
    } catch (error) {
      console.error("Failed to load groups:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const isUserMemberOfGroup = (groupId: string): boolean => {
    return userGroups.some(userGroup => userGroup.id === groupId);
  };

  const handleJoinGroup = async (request: JoinGroupRequest) => {
    try {
      // find group to track loading state
      const groupBeingJoined = groups.find(g => g.inviteCode === request.inviteCode);
      if (groupBeingJoined) {
        setJoiningGroupId(groupBeingJoined.id);
      }
      
      const joinedGroup = await joinGroup(request);
      await loadGroups(); // reload to update list
      showJoinGroupSuccessToast(joinedGroup.name);
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : "Failed to join group";
      console.error("Failed to join group:", errorMessage);
    } finally {
      setJoiningGroupId(null);
    }
  };

  return (
    <div className="min-h-svh">
      <NavBar />
      <div className="p-6 max-w-6xl mx-auto">
        <div className="mb-8">
          <div className="flex items-center gap-4 mb-4">
            <Button asChild variant="outline" size="sm">
              <Link to="/dashboard">
                <ArrowLeft className="h-4 w-4 mr-2" />
                Back to Dashboard
              </Link>
            </Button>
          </div>
          <h1 className="text-3xl font-bold mb-2">Browse Groups</h1>
          <p className="text-muted-foreground">
            Find your fellow pubcrawlers!
          </p>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>All Groups</CardTitle>
          </CardHeader>
          <CardContent>
            {/* Search Bar */}
            <div className="relative mb-6">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                placeholder="Search groups by name..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>

            {isLoading ? (
              <div className="flex items-center justify-center py-8">
                <div className="text-muted-foreground">Loading groups...</div>
              </div>
            ) : (
              <BrowseGroupScrollable
                groups={filteredGroups}
                onJoinGroup={handleJoinGroup}
                joiningGroupId={joiningGroupId || undefined}
                isUserMemberOfGroup={isUserMemberOfGroup}
              />
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
