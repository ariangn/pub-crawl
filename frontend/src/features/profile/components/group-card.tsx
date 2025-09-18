import { Card, CardContent } from "@/components/ui/card";
import type { Group } from "../entity";
import { LeaveGroupDialog } from "./leave-group-dialog";

interface GroupCardProps {
  group: Group;
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
              <h3 className="font-semibold text-lg truncate">{group.name}</h3>
              <p className="text-xs text-muted-foreground">
                Created {new Date(group.createdAt).toLocaleDateString()}
              </p>
            </div>
          </div>
          
          {/* leave button */}
          <LeaveGroupDialog
            groupName={group.name}
            onConfirmLeave={onLeave}
            isLoading={isLeaving}
          />
        </div>
      </CardContent>
    </Card>
  );
}
