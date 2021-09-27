import {useEffect, useState} from 'react';
// import {toaster} from 'evergreen-ui';

export function useErrorHandler() {
  const [error, setError] = useState(null);
  useEffect(() => {
    if (error != null) {
      toaster.danger(error.message);
      setError(null);
    }
  }, [error, setError]);
  return {setError};
}