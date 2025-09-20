import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Calendar, Users } from "lucide-react";
import type { Group, JoinGroupRequest } from "../entity";
import { JoinGroupDialog } from "./join-group-dialog";

interface BrowseGroupCardProps {
  group: Group;
  onJoinGroup: (request: JoinGroupRequest) => void;
  isJoining?: boolean;
  isMember?: boolean;
}

export function BrowseGroupCard({ group, onJoinGroup, isJoining = false, isMember = false }: BrowseGroupCardProps) {
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  return (
    <Card className="hover:shadow-md transition-shadow">
      <CardContent className="p-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center text-primary font-semibold text-lg">
              {group.pfpUrl ? (
                <img src={group.pfpUrl} alt={group.name} className="h-12 w-12 rounded-full object-cover" />
              ) : (
                group.name.charAt(0).toUpperCase()
              )}
            </div>
            
            <div className="flex-1 min-w-0">
              <h3 className="font-semibold text-lg truncate">{group.name}</h3>
              {group.description && (
                <p className="text-sm text-muted-foreground mt-1 line-clamp-2">
                  {group.description}
                </p>
              )}
              <div className="flex items-center space-x-4 text-sm text-muted-foreground mt-1">
                <div className="flex items-center space-x-1">
                  <Calendar className="h-3 w-3" />
                  <span>Created {formatDate(group.createdAt)}</span>
                </div>
                <div className="flex items-center space-x-1">
                  <Users className="h-3 w-3" />
                  <span>Code: {group.inviteCode}</span>
                </div>
              </div>
            </div>
          </div>
          
          {isMember ? (
            <Button
              variant="outline"
              className="ml-4"
              onClick={() => {
                // TODO: group page
                console.log(`Navigate to group ${group.id}`);
              }}
            >
              Go to Group
            </Button>
          ) : (
            <JoinGroupDialog
              onJoinGroup={onJoinGroup}
              isLoading={isJoining}
            />
          )}
        </div>
      </CardContent>
    </Card>
  );
}
