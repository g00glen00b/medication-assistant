import {useEffect, useState} from 'react';
import {notification} from 'antd';

export function useSuccessHandler() {
  const [success, setSuccess] = useState(null);
  useEffect(() => {
    if (success != null) {
      notification.success({
        message: success.message
      })
      setSuccess(null);
    }
  }, [success]);
  return {setSuccess};
}