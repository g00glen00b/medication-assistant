import {AutoComplete, DatePicker, Form, InputNumber, Select} from 'antd';
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

export const AvailabilityForm = ({formId, onSubmit, initialValues = {initialQuantity: 0, quantity: 0, medicationName: null, quantityTypeId: null, expiryDate: null}, isMedicationVisible = true}) => {
  const [form] = Form.useForm();
  const [searchValue, setSearchValue] = useState(null);
  const [medicationResponse, setMedicationResponse] = useState({content: []});
  const [quantityTypeResponse, setQuantityTypeResponse] = useState({content: []});
  useMedicationQuery(searchValue, 1, 10, setMedicationResponse);
  useFindAllQuantityTypes(1, 10, setQuantityTypeResponse);
  const options = medicationResponse.content.map(({id, name}) => ({label: name, value: name, id}));

  return (
    <Form
      id={formId}
      form={form}
      layout="vertical"
      requiredMark="optional"
      initialValues={initialValues}
      style={{
        width: '100%'
      }}
      onFinish={onSubmit}>
      <div
        style={{
          display: 'grid',
          gridTemplateRows: 'repeat(3, auto)',
          gridTemplateColumns: 'repeat(2, 1fr)'
        }}>
        {isMedicationVisible && <Form.Item
          name="medicationName"
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
            onSearch={setSearchValue}/>
        </Form.Item>}
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
            gridColumn: 1,
            paddingRight: '1em'
          }}>
          <Select>
            {quantityTypeResponse.content.map(({id, name}) => (
              <Select.Option
                value={id}
                key={id}>
                {name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item
          name="expiryDate"
          label="Expiry date"
          extra="The date at which the medication should no longer be used"
          style={{
            gridRow: 3,
            gridColumn: 2,
            paddingLeft: '1em'
          }}>
          <DatePicker
            style={{
              width: '100%'
            }}/>
        </Form.Item>
      </div>
    </Form>
  );
};