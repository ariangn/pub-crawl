import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import type { GroupWithMembership } from "../entity/group-with-membership";
import { LeaveGroupDialog } from "./leave-group-dialog";

interface GroupCardProps {
  group: GroupWithMembership;
  onLeave: () => void;
  isLeaving?: boolean;
}

export function GroupCard({ group, onLeave, isLeaving = false }: GroupCardProps) {
  return (
    <Card className="hover:shadow-md transition-shadow">
      <CardContent className="p-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            {/* group avatar */}
            {group.pfpUrl ? (
              <img
                src={group.pfpUrl}
                alt={group.name}
                className="w-12 h-12 rounded-full object-cover"
              />
            ) : (
              <div className="w-12 h-12 rounded-full bg-primary flex items-center justify-center text-primary-foreground text-lg font-semibold">
                {group.name.charAt(0).toUpperCase()}
              </div>
            )}
            
            {/* details */}
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2">
                <h3 className="font-semibold text-lg truncate">{group.name}</h3>
                <Badge variant={group.userRole === 'ADMIN' ? 'default' : 'secondary'}>
                  {group.userRole === 'ADMIN' ? 'Admin' : 'Member'}
                </Badge>
              </div>
              <p className="text-xs text-muted-foreground">
                {group.userRole === 'ADMIN' 
                  ? `Created ${new Date(group.createdAt).toLocaleDateString()}`
                  : `Joined ${new Date(group.joinedAt).toLocaleDateString()}`
                }
              </p>
              {group.description && (
                <p className="text-xs text-muted-foreground mt-1 line-clamp-2">
                  {group.description}
                </p>
              )}
            </div>
          </div>
          
          {/* leave button */}
          {group.userRole !== 'ADMIN' && (
            <LeaveGroupDialog
              groupName={group.name}
              onConfirmLeave={onLeave}
              isLoading={isLeaving}
            />
          )}
        </div>
      </CardContent>
    </Card>
  );
}
