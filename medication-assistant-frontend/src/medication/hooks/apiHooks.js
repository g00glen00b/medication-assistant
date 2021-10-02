import axios from 'axios';
import {useEffect} from 'react';

export function useMedicationQuery(search, page, pageSize, setResponse, setError) {
  useEffect(() => {
    if (search != null) {
      const params = {search, page: page - 1, size: pageSize};
      axios
        .get(`/api/medication/query`, {params})
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError != null && setError(data));
    }
  }, [search, page, pageSize, setResponse, setError]);
}

export function useFindAllQuantityTypes(page, pageSize, setResponse, setError) {
  useEffect(() => {
    const params = {page: page - 1, size: pageSize};
    axios
      .get(`/api/quantity-type`, {params})
      .then(({data}) => setResponse(data))
      .catch(({response: {data}}) => setError != null && setError(data));
  }, [page, pageSize, setResponse, setError]);
}