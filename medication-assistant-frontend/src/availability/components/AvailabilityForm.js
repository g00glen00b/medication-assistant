import {AutoComplete, Form, InputNumber, Select} from 'antd';
import {useFindAllQuantityTypes, useMedicationQuery} from '../../medication/hooks/apiHooks';
import {useState} from 'react';

function moreThanQuantityRule({getFieldValue}) {
  return {
    validator(_, value) {
      if (value == null || getFieldValue('quantity') <= value) {
        return Promise.resolve();
      } else {
        return Promise.reject(new Error('The initial amount should be equal or more than the current amount of doses'));
      }
    }
  }
}

export const AvailabilityForm = ({onSubmit}) => {
  const [form] = Form.useForm();
  const [searchValue, setSearchValue] = useState(null);
  const [medicationId, setMedicationId] = useState(null);
  const [medicationResponse, setMedicationResponse] = useState({content: []});
  const [quantityTypeResponse, setQuantityTypeResponse] = useState({content: []});
  useMedicationQuery(searchValue, 1, 10, setMedicationResponse);
  useFindAllQuantityTypes(1, 10, setQuantityTypeResponse);
  const options = medicationResponse.content.map(({id, name}) => ({label: name, value: name, id}));

  return (
    <Form
      id="availability-form"
      form={form}
      layout="vertical"
      requiredMark="optional"
      initialValues={{
        initialQuantity: 0,
        quantity: 0,
        medication: null,
        quantityType: null
      }}
      style={{
        width: '100%'
      }}
      onFinish={({medication, ...rest}) => onSubmit({...rest, medicationId})}>
      <div
        style={{
          display: 'grid',
          gridTemplateRows: 'repeat(3, auto)',
          gridTemplateColumns: 'repeat(2, 1fr)'
        }}>
        <Form.Item
          name="medication"
          label="Medication"
          extra="For example: Dafalgan"
          rules={[{
            required: true,
            message: 'Please provide the name of the medication'
          }]}
          style={{
            gridRow: 1,
            gridColumnStart: 1,
            gridColumnEnd: 3
          }}>
          <AutoComplete
            options={options}
            onSearch={setSearchValue}
            onSelect={(_, {id}) => setMedicationId(id)}/>
        </Form.Item>
        <Form.Item
          name="quantity"
          label="Amount of doses"
          extra="The amount of doses left within the medication"
          rules={[{
            required: true,
            message: 'Please provide the amount of doses left'
          }]}
          style={{
            gridRow: 2,
            gridColumn: 1,
            paddingRight: '1em'
          }}>
          <InputNumber
            min={0}
            style={{
              width: '100%'
            }}/>
        </Form.Item>
        <Form.Item
          name="initialQuantity"
          label="Initial amount of doses"
          extra="The amount of doses left within the medication"
          rules={[{
            required: true,
            message: 'Please provide the initial amount of doses'
          },
          moreThanQuantityRule]}
          style={{
            gridRow: 2,
            gridColumn: 2,
            paddingLeft: '1em'
          }}>
          <InputNumber
            min={0}
            style={{
              width: '100%'
            }}/>
        </Form.Item>
        <Form.Item
          name="quantityTypeId"
          label="Dose type"
          extra="This can be an amount, a volume in milliliter, ..."
          rules={[{
            required: true,
            message: 'Please provide the dose type'
          }]}
          style={{
            gridRow: 3,
            gridColumnStart: 1,
            gridColumnEnd: 3
          }}>
          <Select>
            {quantityTypeResponse.content.map(({id, name}) => (
              <Select.Option value={id}>
                {name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </div>
    </Form>
  );
};