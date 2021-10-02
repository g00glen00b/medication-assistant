// import {EditIcon, IconButton, majorScale, MinusIcon, PlusIcon, Strong, Table, Text, TrashIcon} from 'evergreen-ui';

import {Button, Table, Typography} from 'antd';
import {DeleteOutlined, EditOutlined, MinusOutlined, PlusOutlined} from '@ant-design/icons';

export const AvailabilityTable = ({availabilities = [], onIncrease = () => {}, onDecrease = () => {}, onEdit = () => {}, onDelete = () => {}}) => {
  const columns = [{
    title: 'Medication',
    dataIndex: 'medication',
    key: 'medication',
    render: ({name}) => <Typography.Text strong>{name}</Typography.Text>
  }, {
    title: 'Quantity',
    dataIndex: 'quantity',
    key: 'quantity',
    render: (_, {quantity, initialQuantity, quantityType: {name}}) => (
      <Typography.Text>{quantity}/{initialQuantity} ({name})</Typography.Text>
    )
  }, {
    title: 'Expiry date',
    dataIndex: 'expiryDate',
    key: 'expiryDate',
    render: expiryDate => (
      expiryDate != null ? (
        <Typography.Text>{expiryDate}</Typography.Text>
      ) : (
        <Typography.Text type="secondary">N/A</Typography.Text>
      )
    )
  }, {
    title: 'Actions',
    dataIndex: 'actions',
    key: 'actions',
    align: 'right',
    render: (_, availability) => (
      <div
        style={{
          display: 'flex',
          justifyContent: 'end'
        }}>
        <Button
          icon={<PlusOutlined />}
          onClick={() => onIncrease(availability)}
          style={{
            marginRight: '0.5em'
          }}/>
        <Button
          icon={<MinusOutlined />}
          onClick={() => onDecrease(availability)}
          style={{
            marginRight: '1em'
          }}/>
        <Button
          icon={<EditOutlined />}
          onClick={() => onEdit(availability)}
          style={{
            marginRight: '1em'
          }}/>
        <Button
          icon={<DeleteOutlined />}
          type="danger"
          onClick={() => onDelete(availability)} />
      </div>
    )
  }];
  return (
    <Table
      columns={columns}
      dataSource={availabilities}
      pagination={false}
      rowKey="id"
      style={{
        boxShadow: '0 0.2em 0.3em 0 rgba(0, 0, 0, .2)'
      }}/>
  );
}