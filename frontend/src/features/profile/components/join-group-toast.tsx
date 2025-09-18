import { toast } from "sonner";

export const showJoinGroupSuccessToast = (groupName: string) => {
  toast.success(`You have successfully joined "${groupName}".`);
};

export const showJoinGroupErrorToast = (error: string) => {
  toast.error(`Failed to join group: ${error}`);
};
