// Inspection disabled to prevent warnings for color attributes on top of Evergreen
// noinspection HtmlUnknownTarget

import {GiPill} from 'react-icons/gi';
import {Link, Pane, Text} from 'evergreen-ui';
import {Link as RouterLink, useLocation} from 'react-router-dom';

export const TopNav = () => (
  <Pane
    is="nav"
    padding="1em"
    display="flex"
    flexDirection="row"
    alignItems="center"
    borderBottom="muted"
    position="fixed"
    top={0}
    elevation={1}
    background="white"
    zIndex={10}
    width="100%">
    <Link
      paddingRight="3em"
      display="flex"
      flexDirection="row"
      alignItems="center"
      is={RouterLink}
      to="/app">
      <GiPill
        style={{marginRight: '0.5em'}}/>
      Medication assistant
    </Link>
    <TopNavLink
      to="/app/availability">
      Availabilities
    </TopNavLink>
    <TopNavLink
      to="/app/schedule">
      Schedule
    </TopNavLink>
  </Pane>
);

const TopNavLink = ({children, to}) => {
  const {pathname} = useLocation();
  const isActive = pathname === to;

  return (
    <>
      {isActive ? (
        <Text
          paddingX="1em"
          fontWeight={500}>
          {children}
        </Text>
      ) : (
        <Link
          paddingX="1em"
          is={RouterLink}
          to={to}>
          {children}
        </Link>
      )}
    </>
  );
};