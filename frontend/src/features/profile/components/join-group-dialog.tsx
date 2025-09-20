import { useState, useEffect } from "react";
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
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Link } from "react-router-dom";
import type { JoinGroupRequest } from "../entity";

interface JoinGroupDialogProps {
  onJoinGroup: (request: JoinGroupRequest) => void;
  isLoading?: boolean;
  error?: string;
  onSuccess?: () => void;
}

export function JoinGroupDialog({ onJoinGroup, isLoading = false, error, onSuccess }: JoinGroupDialogProps) {
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<JoinGroupRequest>({
    inviteCode: "",
  });

  // close dialog on success 
  useEffect(() => {
    if (!isLoading && !error && onSuccess) {
      onSuccess();
      setFormData({ inviteCode: "" });
      setOpen(false);
    }
  }, [isLoading, error, onSuccess]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.inviteCode.trim()) {
      onJoinGroup({
        inviteCode: formData.inviteCode.trim(),
      });
      setFormData({ inviteCode: "" });
      setOpen(false);
    }
  };

  const handleOpenChange = (newOpen: boolean) => {
    if (!isLoading) {
      setOpen(newOpen);
      if (!newOpen) {
        setFormData({ inviteCode: "" });
      }
    }
  };

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogTrigger asChild>
        <Button>Join Group</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle>Join a group</DialogTitle>
            <DialogDescription>
              Enter the invite code to join a crawl group.
            </DialogDescription>
          </DialogHeader>
          
          <div>
            <p className="text-sm text-muted-foreground">
                Or, browse all groups
                <Button asChild variant="ghost" className="p-1 h-auto font-normal underline">
                <Link to="/browse">here</Link>
                </Button>
            </p>
          </div>
          
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="inviteCode">Invite Code</Label>
              <Input
                id="inviteCode"
                value={formData.inviteCode}
                onChange={(e) => setFormData(prev => ({ ...prev, inviteCode: e.target.value }))}
                placeholder="Enter invite code"
                required
                disabled={isLoading}
              />
            </div>
            
          </div>
          
          {error && (
            <div className="text-red-600 text-sm text-center py-2">{error}</div>
          )}
          
          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => setOpen(false)}
              disabled={isLoading}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={isLoading || !formData.inviteCode.trim()}>
              {isLoading ? "Joining..." : "Join Group"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
