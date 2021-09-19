import {EditIcon, IconButton, majorScale, MinusIcon, PlusIcon, Strong, Table, Text, TrashIcon} from 'evergreen-ui';

export const AvailabilityTable = ({availabilities = [], onIncrease = () => {}, onDecrease = () => {}, onEdit = () => {}, onDelete = () => {}}) => {
  return (
    <Table elevation={1}>
      <Table.Head accountForScrollbar={false}>
        <Table.TextHeaderCell>Medication</Table.TextHeaderCell>
        <Table.TextHeaderCell>Quantity</Table.TextHeaderCell>
        <Table.TextHeaderCell>Expiry date</Table.TextHeaderCell>
        <Table.TextHeaderCell
          textAlign="right">
          Actions
        </Table.TextHeaderCell>
      </Table.Head>
      <Table.Body>
        {availabilities.map((availability) => (
          <Table.Row key={availability.medication.id}>
            <Table.TextCell>
              <Strong>{availability.medication.name}</Strong>
            </Table.TextCell>
            <Table.TextCell>
              {availability.quantity}/{availability.initialQuantity} ({availability.quantityType.name})
            </Table.TextCell>
            <Table.TextCell>
              {availability.expiryDate != null ? (
                <Text color={'gray600'}>{availability.expiryDate}</Text>
              ) : (
                <Text>N/A</Text>
              )}
            </Table.TextCell>
            <Table.TextCell
              textAlign="right">
              <IconButton
                icon={PlusIcon}
                marginRight={majorScale(1)}
                type="button"
                onClick={() => onIncrease(availability)} />
              <IconButton
                icon={MinusIcon}
                marginRight={majorScale(2)}
                type="button"
                onClick={() => onDecrease(availability)} />
              <IconButton
                icon={EditIcon}
                marginRight={majorScale(2)}
                type="button"
                onClick={() => onEdit(availability)} />
              <IconButton
                icon={TrashIcon}
                intent="danger"
                type="button"
                onClick={() => onDelete(availability)} />
            </Table.TextCell>
          </Table.Row>
        ))}
      </Table.Body>
    </Table>
  );
}