import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface FavoriteZoneLectureViewFilterProps {
  selectedSubZone: string[];
  selectedSubCategory: string[];
  setZoneModalVisible: () => void;
  setCategoryModalVisible: () => void;
}

export default class FavoriteZoneLectureViewFilter extends React.Component<
  FavoriteZoneLectureViewFilterProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => this.props.setZoneModalVisible()}>
          <Icon name="location-sharp" size={15} color="#4a4a4c" />
          <Text
            numberOfLines={1}
            style={{color: '#4a4a4c', marginLeft: 5, width: '50%'}}>
            {this.props.selectedSubZone.length > 0
              ? this.props.selectedSubZone
              : '전체지역'}
          </Text>
          {this.props.selectedSubZone.length > 1 && (
            <Text style={this.styles.text}>{`외 ${
              this.props.selectedSubZone.length - 1
            }곳`}</Text>
          )}
        </TouchableOpacity>

        <TouchableOpacity
          style={this.styles.button2}
          onPress={() => this.props.setCategoryModalVisible()}>
          <Icon name="football" size={15} color="#4a4a4c" />
          <Text
            numberOfLines={1}
            style={{color: '#4a4a4c', marginLeft: 5, width: '50%'}}>
            {this.props.selectedSubCategory.length > 0
              ? this.props.selectedSubCategory[0]
              : '전체'}
          </Text>
          {this.props.selectedSubCategory.length > 1 && (
            <Text style={this.styles.text2}>{`외 ${
              this.props.selectedSubCategory.length - 1
            }곳`}</Text>
          )}
        </TouchableOpacity>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      marginLeft: 28,
      marginTop: 20,
      marginBottom: 20,
    },
    button: {
      backgroundColor: '#f5f5f7',
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'flex-start',
      paddingHorizontal: 10,
      borderRadius: 6,
      height: 38,
      width: '45%',
    },
    text: {
      color: '#4a4a4c',
      marginLeft: 5,
    },
    button2: {
      backgroundColor: '#f5f5f7',
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'flex-start',
      paddingHorizontal: 10,
      marginLeft: 15,
      borderRadius: 6,
      height: 38,
      width: '45%',
    },
    text2: {
      color: '#4a4a4c',
      marginLeft: 5,
    },
  });
}
