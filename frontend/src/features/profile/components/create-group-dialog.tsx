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
import { Textarea } from "@/components/ui/textarea";
import type { CreateGroupRequest } from "../entity";

interface CreateGroupDialogProps {
  onCreateGroup: (request: CreateGroupRequest) => void;
  isLoading?: boolean;
  error?: string;
  onSuccess?: () => void;
}

export function CreateGroupDialog({ onCreateGroup, isLoading = false, error, onSuccess }: CreateGroupDialogProps) {
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<CreateGroupRequest>({
    name: "",
    pfpUrl: "",
    description: "",
  });

  // close dialog on success 
  useEffect(() => {
    if (!isLoading && !error && onSuccess) {
      onSuccess();
      setFormData({ name: "", pfpUrl: "", description: "" });
      setOpen(false);
    }
  }, [isLoading, error, onSuccess]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.name.trim()) {
      onCreateGroup({
        name: formData.name.trim(),
        pfpUrl: formData.pfpUrl?.trim() || undefined,
        description: formData.description?.trim() || undefined,
      });
    }
  };

  const handleOpenChange = (newOpen: boolean) => {
    if (!isLoading) {
      setOpen(newOpen);
      if (!newOpen) {
        setFormData({ name: "", pfpUrl: "", description: "" });
      }
    }
  };

  return (
    <Dialog open={open} onOpenChange={handleOpenChange}>
      <DialogTrigger asChild>
        <Button>Create Group</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle>Create a new group</DialogTitle>
            <DialogDescription>
              Make sure to choose a unique name for your group.
            </DialogDescription>
          </DialogHeader>
          
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="name">Group Name</Label>
              <Input
                id="name"
                value={formData.name}
                onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                placeholder="Enter group name"
                required
                disabled={isLoading}
              />
            </div>
            
            <div className="grid gap-2">
              <Label htmlFor="pfpUrl">Avatar URL (Optional)</Label>
              <Input
                id="pfpUrl"
                value={formData.pfpUrl}
                onChange={(e) => setFormData(prev => ({ ...prev, pfpUrl: e.target.value }))}
                placeholder="https://example.com/image.jpg"
                type="url"
                disabled={isLoading}
              />
            </div>
            
            <div className="grid gap-2">
              <Label htmlFor="description">Description (Optional)</Label>
              <Textarea
                id="description"
                value={formData.description}
                onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
                placeholder="Describe your group..."
                maxLength={500}
                disabled={isLoading}
                className="min-h-[80px]"
              />
              <p className="text-xs text-muted-foreground">
                {formData.description?.length || 0}/500 characters
              </p>
            </div>
          </div>
          
          {error && (
            <div className="text-red-600 text-sm pb-4">{error}</div>
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
            <Button type="submit" disabled={isLoading || !formData.name.trim()}>
              {isLoading ? "Creating..." : "Create Group"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
