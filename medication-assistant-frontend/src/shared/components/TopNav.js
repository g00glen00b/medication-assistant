// Inspection disabled to prevent warnings for color attributes on top of Evergreen
// noinspection HtmlUnknownTarget

import {GiPill} from 'react-icons/gi';
import {Link, useLocation} from 'react-router-dom';
import {Typography} from 'antd';
import {neutral} from '../theme/colorPalette';

export const TopNav = () => (
  <nav
    style={{
      padding: '1em',
      display: 'flex',
      flexDirection: 'row',
      alignItems: 'center',
      position: 'fixed',
      top: 0,
      boxShadow: '0 0 0.5em 0 rgba(0, 0, 0, .5)',
      background: 'white',
      borderBottomColor: neutral[3],
      borderBottomStyle: 'solid',
      borderBottomWidth: '1px',
      zIndex: 10,
      width: '100%'
    }}>
    <Link
      style={{
        paddingRight: '3em',
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center'
      }}
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
  </nav>
);

const TopNavLink = ({children, to}) => {
  const {pathname} = useLocation();
  const isActive = pathname === to;

  return (
    <>
      {isActive ? (
        <Typography.Text
          strong
          style={{
            paddingLeft: '1em',
            paddingRight: '1em'
          }}>
          {children}
        </Typography.Text>
      ) : (
        <Link
          style={{
            paddingLeft: '1em',
            paddingRight: '1em'
          }}
          to={to}>
          {children}
        </Link>
      )}
    </>
  );
};