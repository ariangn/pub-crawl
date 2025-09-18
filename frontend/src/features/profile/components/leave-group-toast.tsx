import { toast } from "sonner";

export const showLeaveGroupSuccessToast = (groupName: string) => {
  toast.success(`You have successfully left "${groupName}".`);
};

export const showLeaveGroupErrorToast = (error: string) => {
  toast.error(`Failed to leave group: ${error}`);
};
