import FilterModal from '@/screens/modal/FilterModal';
import ZoneSelectModal from '@/screens/modal/ZoneSelectModal';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface LectureSearchResultViewFilterProps {
  selectedSubZone: string;
  filterCondition: string;
  searchPriceCondition: string;
  setSubZone: (sub: string) => void;
  setFilterCondition: (filterCondition: string) => void;
  setSearchPriceCondition: (searchPriceCondition: string) => void;
}

interface LectureSearchResultViewFilterStates {
  zoneModalVisible: boolean;
  filterModalVisible: boolean;
}

export default class LectureSearchResultViewFilter extends React.Component<
  LectureSearchResultViewFilterProps,
  LectureSearchResultViewFilterStates
> {
  constructor(props: any) {
    super(props);
    this.state = {zoneModalVisible: false, filterModalVisible: false};
  }
  render() {
    return (
      <View style={{flexDirection: 'row', marginLeft: 28, marginTop: 10}}>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() =>
            this.setState({zoneModalVisible: !this.state.zoneModalVisible})
          }>
          <Icon name="location-sharp" size={12} color="#4a4a4c" />
          <Text style={{color: '#4a4a4c', marginLeft: 5}}>
            {this.props.selectedSubZone
              ? this.props.selectedSubZone
              : '전체지역'}
          </Text>
        </TouchableOpacity>
        <ZoneSelectModal
          modalVisible={this.state.zoneModalVisible}
          setModalVisible={(visible: boolean) => {
            this.setState({zoneModalVisible: visible});
          }}
          setParentSubZone={(sub: string) => {
            this.props.setSubZone(sub);
          }}
        />
        <TouchableOpacity
          style={this.styles.button2}
          onPress={() => {
            this.setState({
              filterModalVisible: !this.state.filterModalVisible,
            });
          }}>
          <Icon name="filter" size={12} color="#4a4a4c" />
          <Text style={{color: '#4a4a4c', marginLeft: 5}}>필터</Text>
        </TouchableOpacity>

        <FilterModal
          modalVisible={this.state.filterModalVisible}
          filterCondition={this.props.filterCondition}
          searchPriceCondition={this.props.searchPriceCondition}
          setModalVisible={(visible: boolean) => {
            this.setState({filterModalVisible: visible});
          }}
          setFilterCondition={(filterCondition: string) => {
            this.props.setFilterCondition(filterCondition);
          }}
          setSearchPriceCondition={(searchPriceCondition: string) =>
            this.props.setSearchPriceCondition(searchPriceCondition)
          }
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
    },
    button2: {
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
