import axios from 'axios';
import {useEffect, useState} from 'react';

export function useCreateUserApi(request, setResponse, setError = () => {}) {
  useEffect(() => {
    if (request != null) {
      axios
        .post('/api/user', request)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError(data));
    }
  }, [request, setResponse, setError]);
}

export function useLoginApi(username, password, setResponse, setError = () => {}) {
  useEffect(() => {
    if (username != null && password != null) {
      const auth = {username, password};
      axios
        .get('/api/user/current', {auth})
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError(data));
    }
  }, [username, password, setResponse, setError]);
}

export function useCurrentUser(setResponse, setError = () => {}) {
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (loading) {
      axios
        .get('/api/user/current')
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError(data))
        .finally(() => setLoading(false));
    }
  }, [setResponse, setError, loading]);
  return {setLoading};
}