import { toast } from "sonner";

export const showCreateGroupSuccessToast = (groupName: string) => {
  toast.success(`"${groupName}" has been created successfully.`);
};

export const showCreateGroupErrorToast = (error: string) => {
  toast.error(`Failed to create group: ${error}`);
};
