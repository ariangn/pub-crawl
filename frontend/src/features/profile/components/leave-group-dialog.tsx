import { useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";

interface LeaveGroupDialogProps {
  groupName: string;
  onConfirmLeave: () => void;
  isLoading?: boolean;
}

export function LeaveGroupDialog({ groupName, onConfirmLeave, isLoading = false }: LeaveGroupDialogProps) {
  const [open, setOpen] = useState(false);

  const handleConfirm = () => {
    onConfirmLeave();
    setOpen(false);
  };

  const handleOpenChange = (newOpen: boolean) => {
    if (!isLoading) {
      setOpen(newOpen);
    }
  };

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogTrigger asChild>
        <Button
          variant="outline"
          size="sm"
        >
          Leave
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Leave Group</DialogTitle>
          <DialogDescription>
            Are you sure you want to leave <strong>"{groupName}"</strong>? 
            You will need an invite code to rejoin this group.
          </DialogDescription>
        </DialogHeader>
        
        <DialogFooter>
          <Button
            variant="outline"
            onClick={() => setOpen(false)}
            disabled={isLoading}
          >
            Cancel
          </Button>
          <Button
            variant="destructive"
            onClick={handleConfirm}
            disabled={isLoading}
          >
            {isLoading ? "Leaving..." : "Leave Group"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
