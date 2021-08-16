import axios from 'axios';
import {useEffect, useState} from 'react';

export function useCreateUserApi(request) {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  useEffect(() =>  {
    if (request != null) {
      (async function () {
        try {
          setResponse(await axios.post('/api/user', request));
          setError(null);
        } catch (err) {
          setResponse(null);
          setError(err);
        }
      })();
    }
  }, [request]);
  return {response, error};
}