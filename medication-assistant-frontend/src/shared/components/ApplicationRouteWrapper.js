import {Redirect, Route, Switch, useRouteMatch} from 'react-router-dom';
import {TopNav} from './TopNav';
import {useContext} from 'react';
import {AuthenticationContext} from '../../authentication/components/AuthenticationContext';
import {AvailabilityPage} from '../../availability/pages/AvailabilityPage';
import {neutral} from '../theme/colorPalette';

export const ApplicationRouteWrapper = () => {
  const {url} = useRouteMatch();
  const {state: {user}} = useContext(AuthenticationContext);
  return (
    <>
      <TopNav/>
      <div
        style={{
          background: neutral[1],
          minHeight: 'calc(100vh - 53px)',
          padding: '2em',
          marginTop: '53px'
        }}>
        <Switch>
          {user == null && <Redirect to={`/login`} />}
          <Route exact path={`${url}/availability`}>
            <AvailabilityPage/>
          </Route>
          <Route exact path={url}>
            <Redirect to={`${url}/availability`} />
          </Route>
        </Switch>
      </div>
    </>
  );
};