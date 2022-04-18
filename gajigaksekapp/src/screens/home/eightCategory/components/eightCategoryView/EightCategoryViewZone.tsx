import ZoneSelectModal from '@/screens/modal/ZoneSelectModal';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface EightCategoryViewZoneProps {
  selectedSubZone: string;
  setParentSubZone: (sub: string) => void;
}

interface EightCategoryViewZoneStates {
  modalVisible: boolean;
}

export default class EightCategoryViewZone extends React.Component<
  EightCategoryViewZoneProps,
  EightCategoryViewZoneStates
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
          onPress={() =>
            this.setState({
              modalVisible: !this.state.modalVisible,
            })
          }>
          <Icon name="location-sharp" size={12} color="#4a4a4c" />
          <Text style={{color: '#4a4a4c', marginLeft: 5}}>
            {this.props.selectedSubZone
              ? this.props.selectedSubZone
              : '전체지역'}
          </Text>
        </TouchableOpacity>
        <ZoneSelectModal
          modalVisible={this.state.modalVisible}
          setModalVisible={(visible: boolean) => {
            this.setState({modalVisible: visible});
          }}
          setParentSubZone={(sub: string) => {
            this.props.setParentSubZone(sub);
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
    },
  });
}
