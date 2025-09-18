import { ScrollArea } from "@/components/ui/scroll-area";
import { GroupCard } from "./group-card";
import type { Group } from "../entity";

interface GroupScrollableProps {
  groups: Group[];
  onLeaveGroup: (groupId: string) => void;
  leavingGroupId?: string;
}

export function GroupScrollable({ groups, onLeaveGroup, leavingGroupId }: GroupScrollableProps) {
  if (groups.length === 0) {
    return (
      <div className="flex items-center justify-center h-32 text-muted-foreground">
        <p>No groups yet. Create or join a group to get started!</p>
      </div>
    );
  }

  return (
    <ScrollArea className="h-64 w-full">
      <div className="space-y-3 pr-4">
        {groups.map((group) => (
          <GroupCard
            key={group.id}
            group={group}
            onLeave={() => onLeaveGroup(group.id)}
            isLeaving={leavingGroupId === group.id}
          />
        ))}
      </div>
    </ScrollArea>
  );
}
