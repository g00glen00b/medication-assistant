import axios from 'axios';
import {useEffect, useState} from 'react';

export function useCreateUserApi(request) {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  useEffect(() => {
    if (request != null) {
      axios
        .post('/api/user', request)
        .then(setResponse)
        .catch(setError);
    }
  }, [request]);
  return {response, error};
}

export function useLoginApi(username, password) {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  useEffect(() => {
    if (username != null && password != null) {
      const auth = {username, password};
      axios
        .get('/api/user/current', {auth})
        .then(setResponse)
        .catch(setError);
    }
  }, [username, password]);
  return {response, error};
}

export function useCurrentUser(loadingUser) {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  useEffect(() => {
    if (loadingUser) {
      axios
        .get('/api/user/current')
        .then(setResponse)
        .catch(setError);
    }
  }, [loadingUser]);
  return {response, error};
}
