import {EditIcon, IconButton, majorScale, MinusIcon, PlusIcon, Strong, Table, TrashIcon} from 'evergreen-ui';

export const AvailabilityTable = ({availabilities = []}) => (
  <Table elevation={1}>
    <Table.Head>
      <Table.SearchHeaderCell/>
      <Table.TextHeaderCell>Quantity</Table.TextHeaderCell>
      <Table.TextHeaderCell
        textAlign="right">
        Actions
      </Table.TextHeaderCell>
    </Table.Head>
    <Table.Body>
      {availabilities.map(({medication, quantityType, quantity}) => (
        <Table.Row key={medication.id}>
          <Table.TextCell>
            <Strong>{medication.name}</Strong>
          </Table.TextCell>
          <Table.TextCell>
            {quantity} ({quantityType.name})
          </Table.TextCell>
          <Table.TextCell
            textAlign="right">
            <IconButton
              icon={PlusIcon}
              marginRight={majorScale(1)} />
            <IconButton
              icon={MinusIcon}
              marginRight={majorScale(2)} />
            <IconButton
              icon={EditIcon}
              marginRight={majorScale(2)} />
            <IconButton
              icon={TrashIcon}
              intent="danger"
              marginRight={majorScale(2)} />
          </Table.TextCell>
        </Table.Row>
      ))}
    </Table.Body>
  </Table>
);