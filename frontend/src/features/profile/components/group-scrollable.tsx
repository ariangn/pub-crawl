import { useAutoAnimate } from "@formkit/auto-animate/react";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Button } from "@/components/ui/button";
import { GroupCard } from "./group-card";
import { Link } from "react-router-dom";
import type { GroupWithMembership } from "../entity/group-with-membership";

interface GroupScrollableProps {
  groups: GroupWithMembership[];
  onLeaveGroup: (groupId: string) => void;
  leavingGroupId?: string;
}

export function GroupScrollable({ groups, onLeaveGroup, leavingGroupId }: GroupScrollableProps) {
  const [parent] = useAutoAnimate();

  if (groups.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center h-32 text-muted-foreground space-y-2">
        <p>No groups yet. Create a group, or browse all groups
          <Button asChild variant="ghost" className="p-1 h-auto font-normal underline">
            <Link to="/browse">here</Link>
          </Button>
        </p>
      </div>
    );
  }

  return (
    <ScrollArea className="h-64 w-full">
      <div ref={parent} className="space-y-3 pr-4">
        {groups.map((group) => (
          <GroupCard
            key={group.id}
            group={group}
            onLeave={() => onLeaveGroup(group.id)}
            isLeaving={leavingGroupId === group.id}
          />
        ))}
        <div className="flex justify-center pt-2">
          <p className="text-sm text-muted-foreground">
            Or, browse all groups
            <Button asChild variant="ghost" className="p-1 h-auto font-normal underline">
              <Link to="/browse">here</Link>
            </Button>
          </p>
        </div>
      </div>
    </ScrollArea>
  );
}
