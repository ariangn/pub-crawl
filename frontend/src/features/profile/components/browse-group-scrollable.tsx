import { useAutoAnimate } from "@formkit/auto-animate/react";
import { ScrollArea } from "@/components/ui/scroll-area";
import { BrowseGroupCard } from "./browse-group-card";
import type { Group, JoinGroupRequest } from "../entity";

interface BrowseGroupScrollableProps {
  groups: Group[];
  onJoinGroup: (request: JoinGroupRequest) => void;
  joiningGroupId?: string;
  isUserMemberOfGroup: (groupId: string) => boolean;
}

export function BrowseGroupScrollable({ 
  groups, 
  onJoinGroup, 
  joiningGroupId,
  isUserMemberOfGroup
}: BrowseGroupScrollableProps) {
  const [parent] = useAutoAnimate();

  if (groups.length === 0) {
    return (
      <div className="flex items-center justify-center py-8">
        <div className="text-muted-foreground text-center">
          <p className="text-lg font-medium">No groups found</p>
          <p className="text-sm">Try adjusting your search or create a new group.</p>
        </div>
      </div>
    );
  }

  return (
    <ScrollArea className="h-[400px] w-full">
      <div ref={parent} className="space-y-4 pr-4">
        {groups.map((group) => (
          <BrowseGroupCard
            key={group.id}
            group={group}
            onJoinGroup={onJoinGroup}
            isJoining={joiningGroupId === group.id}
            isMember={isUserMemberOfGroup(group.id)}
          />
        ))}
      </div>
    </ScrollArea>
  );
}
