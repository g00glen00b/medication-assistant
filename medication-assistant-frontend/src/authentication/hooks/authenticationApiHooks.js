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

export function useLoginApi(username, password) {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  useEffect(() =>  {
    if (username != null && password != null) {
      (async function () {
        try {
          const auth = {username, password};
          setResponse(await axios.get('/api/user/current', {auth}));
          setError(null);
        } catch (err) {
          setResponse(null);
          setError(err);
        }
      })();
    }
  }, [username, password]);
  return {response, error};
}

export function useCurrentUser() {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  useEffect(() =>  {
    (async function () {
      try {
        setResponse(await axios.get('/api/user/current'));
        setError(null);
      } catch (err) {
        setResponse(null);
        setError(err);
      }
    })();
  }, []);
  return {response, error};
}