import {useEffect, useState} from 'react';
import {notification} from 'antd';

export function useErrorHandler() {
  const [error, setError] = useState(null);
  useEffect(() => {
    if (error != null) {
      notification.error({
        message: error.message
      })
      setError(null);
    }
  }, [error]);
  return {setError};
}