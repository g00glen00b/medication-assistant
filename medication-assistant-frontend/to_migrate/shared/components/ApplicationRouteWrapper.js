import {Redirect, Route, Switch, useRouteMatch} from 'react-router-dom';
import {TopNav} from './TopNav';
import {AvailabilityPage} from '../../avaialbility/pages/AvailabilityPage';
// import {Pane} from 'evergreen-ui';
import {useContext} from 'react';
import {AuthenticationContext} from '../../authentication/components/AuthenticationContext';

export const ApplicationRouteWrapper = () => {
  const {url} = useRouteMatch();
  const {state: {user}} = useContext(AuthenticationContext);
  return (
    <>
      <TopNav/>
      <Pane
        background={"gray100"}
        minHeight="calc(100vh - 53px)"
        padding="2em"
        marginTop="53px">
        <Switch>
          {user == null && <Redirect to={`/login`} />}
          <Route exact path={`${url}/availability`}>
            <AvailabilityPage/>
          </Route>
          <Route exact path={url}>
            <Redirect to={`${url}/availability`} />
          </Route>
        </Switch>
      </Pane>
    </>
  );
};