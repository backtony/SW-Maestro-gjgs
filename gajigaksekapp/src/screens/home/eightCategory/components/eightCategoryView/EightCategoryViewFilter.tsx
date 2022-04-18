import FilterModal from '@/screens/modal/FilterModal';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface EightCategoryViewFilterProps {
  filterCondition: string;
  searchPriceCondition: string;
  setFilterCondition: (filterCondition: string) => void;
  setSearchPriceCondition: (searchPriceCondition: string) => void;
}

interface EightCategoryViewFilterStates {
  modalVisible: boolean;
}

export default class EightCategoryViewFilter extends React.Component<
  EightCategoryViewFilterProps,
  EightCategoryViewFilterStates
> {
  constructor(props: any) {
    super(props);
    this.state = {modalVisible: false};
  }
  render() {
    return (
      <View>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.setState({
              modalVisible: !this.state.modalVisible,
            });
          }}>
          <Icon name="filter" size={12} color="#4a4a4c" />
          <Text style={{color: '#4a4a4c', marginLeft: 5}}>필터</Text>
        </TouchableOpacity>

        <FilterModal
          modalVisible={this.state.modalVisible}
          filterCondition={this.props.filterCondition}
          searchPriceCondition={this.props.searchPriceCondition}
          setModalVisible={(visible: boolean) => {
            this.setState({modalVisible: visible});
          }}
          setFilterCondition={(filterCondition: string) => {
            this.props.setFilterCondition(filterCondition);
          }}
          setSearchPriceCondition={(searchPriceCondition: string) => {
            this.props.setSearchPriceCondition(searchPriceCondition);
          }}
        />
      </View>
    );
  }
  private styles = StyleSheet.create({
    button: {
      backgroundColor: '#f5f5f7',
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'center',
      paddingHorizontal: 10,
      borderRadius: 6,
      height: 32,
      marginLeft: 10,
    },
  });
}
